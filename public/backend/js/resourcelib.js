var progressBar = null;
var progress=1;
	
var fileUploadLoading = function(file){
		var fileSize = parseInt(file.size)/1024;
		
		if($(".upload-list").find("li").length > 0){
			var childCont = '	<li>'+
							'		<div class="bl u-info">'+
							'			<p class="u-name txt-ell">'+file.name+'</p>'+
							'			<p class="u-total">'+fileSize.toFixed(1)+"kb"+'</p>'+
							'		</div>'+
							'		<div class="bl u-progress">'+
							'			<div class="progress">'+
							'		  		<div class="progress-bar progress-bar-striped active" id="progress'+file.progressBarId+'">'+
							'				</div>'+
							'		  	</div>'+
							'		</div>'+
							'		<div class="bl u-del">'+
							'			<a href="javascript:;" class="u-btn-del"><i class="iconfont icon-iconfonticonfontclose"></i></a>'+
							'		</div>'+
							'	</li>';
			$(".upload-list").append(childCont);
		}else{
			var cont =  '<ul class="upload-list">'+
						'	<li>'+
						'		<div class="bl u-info">'+
						'			<p class="u-name txt-ell">'+file.name+'</p>'+
						'			<p class="u-total">'+fileSize.toFixed(1)+"kb"+'</p>'+
						'		</div>'+
						'		<div class="bl u-progress">'+
						'			<div class="progress">'+
						'		  		<div class="progress-bar progress-bar-striped active" id="progress'+file.progressBarId+'">'+
						'				</div>'+
						'		  	</div>'+
						'		</div>'+
						'		<div class="bl u-del">'+
						'			<a href="javascript:;" class="u-btn-del"><i class="iconfont icon-iconfonticonfontclose"></i></a>'+
						'		</div>'+
						'	</li>'+
						'</ul>';
	
			layer.open({
				type: 1,
				title: '上传文件',
				area: ['638px', '280px'],
				shade: 0,
				content: cont
			});
		}
		progressBar=self.setInterval(function(){
			$("#progress"+file.progressBarId).css("width",progress+"%");
			if(progress<=90){
				progress++;
			}
		}, 100)
	}

	
	
	
	
	//分页功能
	$("#libPageCode").createPage({
		pageCount:$("#libPageCode").attr("pageCount"),
		current:1,
		backFn:function(p){
			$("#libPageCode").attr("page",p);
			 var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
			$.post("/edu/getResourceData",{page:p,folderId:folderId,type:"全部"},function(json){
				$('.re-list').html(json.html);
			})
			
		}
	});
	
	
	
	
$(function(){
	
	
	//切换资源库
	$(".nav-cont").on("click","li",function(){
		 $(this).addClass('selected').siblings().removeClass('selected');
		 var groupId = $(this).attr("data-id");
		 $.post("/edu/back/jsonLib",{groupId:groupId},function(json){
			 if(json.succ){
				 $(".main").html("");
				 $(".main").append(json.html);
				$("#js-upload-file").uploadify({
			        multi : false,
			        checkExisting : false,
			        fileObjName : 'qqfile',
			        buttonClass: 'btn',
			        width : 80,
			        height : 29,
			        formData : {bucketName: "smallfiles", source: "SOOC"},
			        fileSizeLimit : 0,//上传文件大小限制
			        fileTypeDesc : '图片文件',
			        buttonText: '上传文件',
			        swf : '/public/js/uploadify/uploadify.swf',
			        uploader: "http://oss.iclass.cn/formFile",
			        queueID : "UploadQueue",
			        
			        onUploadStart:function(file){
			        	fileUploadLoading(file);
			        	$(".icon-iconfonticonfontclose").on("click",function(){
			        		$("#js-upload-file").uploadify("cancel");
			        		$(this).parent().parent().parent().remove();
			        		clearInterval(progressBar);
			        	})
			        },
			        onUploadSuccess : function(file,data,response){
			        	clearInterval(progressBar);
			        	$(".progress-bar-striped").css("width",100+"%");
			        	var jsonobj=eval('('+data+')');
			            var fileURL=jsonobj.html;
			            var fileName=file.name;
			            var fileSize = parseInt(file.size)/1024;
			            var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
			            $.post("/edu/createResource",{folderId:folderId,fileName:fileName,fileSize:fileSize.toFixed(1),url:fileURL},function(json){
			            	if(json.resultStatus){
			            		layer.closeAll();
			            		$("#folderList .selected").trigger("click");
			            	}
			            })
			            
			            
			        }
			    });
				
				$("#libPageCode").createPage({
					pageCount:$("#libPageCode").attr("pageCount"),
					current:1,
					backFn:function(p){
						$("#libPageCode").attr("page",p);
						 var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
						$.post("/edu/getResourceData",{page:p,folderId:folderId,type:"全部"},function(json){
							$('.re-list').html(json.html);
						})
						
					}
				});
				
			 }
		 })
	})
	
	
	//切换文件夹
	$(".main").on("click","#folderList li",function(){
		 $(this).addClass('selected').siblings().removeClass('selected');
		 var folderId = $(this).attr("data-folder-id");
			$.post("/edu/back/folderSwitch",{folderId:folderId},function(json){
				if(json.succ == "true"){
					$(".re-right").html("");
					$(".re-right").html(json.html);
					$("#libPageCode").createPage({
						pageCount:$("#libPageCode").attr("pageCount"),
						current:1,
						backFn:function(p){
							$("#libPageCode").attr("page",p);
							 var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
							$.post("/edu/getResourceData",{page:p,folderId:folderId,type:"全部"},function(json){
								$('.re-list').html(json.html);
							})
							
						}
					});
					
				}
			})
	});
	
	//新建文件夹
	$(".main").on("click",".js-create-folder",function(){
		var cont =  '<div class="toper-form">'+
					'    <div class="form-group">'+
					'    	<label class="lab wa">文件夹名称</label>'+
					'		<div class="inb"><input type="text" class="cont ipt" id="folderName"></div>'+
					'	</div>'+
					'</div>';
			layer.open({
				type: 1,
				title: '新建文件夹',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var name = $("#folderName").val().trim();
					if(name.length < 1){
						layer.msg("文件夹名称不能为空！");
						return false;
					}
					var groupId = $(".nav-cont").find("li.selected").attr("data-id");
					
					$.post("/edu/addFolder",{name:name,groupId:groupId},function(json){
						if(json.resultStatus){
							var data = json.url.split(",");
							$("#folderList").append('<li data-folder-id="'+data[0]+'"><i class="iconfont icon-iconfontcolor52"></i>'+data[1]+'</li>');
							layer.close(index);
							$("#folderList").find("li:last").trigger("click");
						}
					})
					
				},
				no: function(index){
					layer.close(index);
				}
			});
	});
	
	
	if( $("#js-upload-file").length>0){
		$("#js-upload-file").uploadify({
	        multi : false,
	        checkExisting : false,
	        fileObjName : 'qqfile',
	        buttonClass: 'btn',
	        width : 80,
	        height : 29,
	        formData : {bucketName: "smallfiles", source: "SOOC"},
	        fileSizeLimit : 0,//上传文件大小限制
	        fileTypeDesc : '图片文件',
	        buttonText: '上传文件',
	        swf : '/public/js/uploadify/uploadify.swf',
	        uploader: "http://oss.iclass.cn/formFile",
	        queueID : "UploadQueue",
	        
	        onUploadStart:function(file){
	        	fileUploadLoading(file);
	        	$(".icon-iconfonticonfontclose").on("click",function(){
	        		$("#js-upload-file").uploadify("cancel");
	        		$(this).parent().parent().parent().remove();
	        		clearInterval(progressBar);
	        	})
	        },
	        onUploadSuccess : function(file,data,response){
	        	clearInterval(progressBar);
	        	$(".progress-bar-striped").css("width",100+"%");
	        	var jsonobj=eval('('+data+')');
	            var fileURL=jsonobj.html;
	            var fileName=file.name;
	            var fileSize = parseInt(file.size)/1024;
	            var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
	            $.post("/edu/createResource",{folderId:folderId,fileName:fileName,fileSize:fileSize.toFixed(1),url:fileURL},function(json){
	            	if(json.resultStatus){
	            		layer.closeAll();
	            		$("#folderList .selected").trigger("click");
	            	}
	            })
	            
	            
	        }
	    });
	 }
	
	// 编辑 & 删除
	$('.main').on('click', '.b-edit', function() {
		var n = $(this).prev().text(),
			ele = $(this).prev(),
			folderId = $(this).attr("data-id"),
			cont =  '<div class="toper-form">'+
					'    <div class="form-group">'+
					'    	<label class="lab wa">文件夹名称</label>'+
					'		<div class="inb"><input type="text" class="cont ipt" id="editFolderName" value="'+ n +'"></div>'+
					'	</div>'+
					'</div>';

		layer.open({
			type: 1,
			title: '修改文件夹名称',
			area: ['420px', 'auto'],
			btn: ['确定', '取消'],
			content: cont,
			yes: function(index) {
				var name = $("#editFolderName").val().trim();
				if(name.length < 1){
					layer.msg("文件夹名称不能为空！");
					return false;
				}
				$.post("/edu/editFolderName",{folderId:folderId,name:name},function(json){
					if(json.resultStatus){
						window.location.reload();
					}
				})

			},
			no: function(index){
				layer.close(index);
			}
		});
	});

	$('.main').on('click', '.b-del', function() {
		var n = $(this).prev().text(),
			ele = $(this).prev(),
			folderId = $(this).attr("data-id"),
			cont =  '<div class="toper-form">'+
					'    <div class="form-group">'+
					'    	<label class="lab wa">删除文件夹时会同时删除子文件，您确定要删除吗？</label>'+
					'	</div>'+
					'</div>';

		layer.open({
			type: 1,
			title: '删除文件夹',
			area: ['420px', 'auto'],
			btn: ['确定', '取消'],
			content: cont,
			yes: function(index) {
				$.post("/edu/deleteFolder",{folderId:folderId},function(json){
					if(json.resultStatus){
						$("#folderList").find("li.selected").remove();
						$(".re-right").empty();
						$("#folderList").find("li:first").trigger("click");
						layer.close(index);
					}
				})
			},
			no: function(index){
				layer.close(index);
			}
		});
	});
	
	
	
	//文件操作相关
	$('.main').on('click', '.type-toggle', function(event) {
        if($(this).hasClass('open')){
            $(this).removeClass('open');
        } else {
            $(this).addClass('open');
        }
        event.stopPropagation();
    });

    
    $('body').click(function(){
         $('.xinxi5').removeClass('open');
         $('.type-toggle').removeClass('open');
    });
	
    
    //详情
    $('.main').on("click",".enlarge",function(){
		var ele=$(this),imgURL=ele.attr("data-url");
		var json = {
				  "title": "", 
				  "id": 123, 
				  "start": 0, 
				  "data": [   
				    {
				      "alt": "",
				      "pid": 666, 
				      "src": imgURL, 
				      "thumb": imgURL
				    }
				  ]
				}
		layer.photos({
			photos: json, 
			shift: 5 
		});
	}) 
	
	//删除
	 $('.main').on("click",".deleteResource",function(){
		var ele=$(this),resourceId=ele.attr("data-id");
		layer.confirm("您确定删除该文件吗？",function(){
        	$.post("/edu/deleteResource",{resourceId:resourceId},function(json){
        		if(json.resultStatus){
        			layer.msg("删除文件成功！");
        			ele.parent().parent().parent().parent().remove();
        		}
        	})
    	})
	}) 
	
	
	
	//另存
	 $('.main').on("click",".saveAsFolder",function(){
	    var resourceId = $(this).attr("data-id");
	    var ele = $(this);
		var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
		$.post("/edu/jsonFolder",{folderId:folderId},function(json){
			if(json.succ == "true"){
				var cont = json.html;
				layer.open({
					type: 1,
					title: '另存资源文件',
					area: ['460px', 'auto'],
					btn: ['确定', '取消'],
					content: cont,
					yes: function(index) {
						var saveFolderId = $("#chooseFolder").val();
						$.post("/edu/saveAsResource",{resourceId:resourceId,folderId:saveFolderId},function(json){
							if(json.resultStatus){
								layer.msg("另存资源文件成功！");
								layer.close(index);
								ele.parent().parent().parent().parent().remove();
							}
							
						})
					},
					no: function(index){
						layer.close(index);
					}
				});
			}
		})
	});
    
    
    //保存到个人资源
    $('.main').on("click",".saveToMyFolder",function(){
	    var resourceId = $(this).attr("data-id");
		$.post("/edu/jsonSaveFolder",{type:'personal'},function(json){
			if(json.succ == "true"){
				var cont = json.html;
				layer.open({
					type: 1,
					title: '保存到个人资源',
					area: ['460px', 'auto'],
					btn: ['确定', '取消'],
					content: cont,
					yes: function(index) {
						var saveFolderId = $("#chooseFolder").val();
						$.post("/edu/fileSaveAndShare",{resourceId:resourceId,folderId:saveFolderId},function(json){
							if(json.resultStatus){
								layer.msg("保存到个人资源成功！");
								layer.close(index);
							}
							
						})
					},
					no: function(index){
						layer.close(index);
					}
				});
			}
		})
	});
    
    //共享到公共资源
    $('.main').on("click",".shareToFolder",function(){
	    var resourceId = $(this).attr("data-id");
		$.post("/edu/jsonSaveFolder",{type:'common'},function(json){
			if(json.succ == "true"){
				var cont = json.html;
				layer.open({
					type: 1,
					title: '共享到公共资源',
					area: ['460px', 'auto'],
					btn: ['确定', '取消'],
					content: cont,
					yes: function(index) {
						var saveFolderId = $("#chooseFolder").val();
						$.post("/edu/fileSaveAndShare",{resourceId:resourceId,folderId:saveFolderId},function(json){
							if(json.resultStatus){
								layer.msg("共享到公共资源成功！");
								layer.close(index);
							}
							
						})
					},
					no: function(index){
						layer.close(index);
					}
				});
				
				$("#chooseLib").on("change",function(){
					var groupId = $("#chooseLib").val();
					$.post("/edu/getLibFolder",{groupId:groupId},function(result){
						$("#chooseFolder").html("");
	                	$.each(result,function(n,value){
	                		$("#chooseFolder").append("<option value='"+value.id+"'>"+value.name+"</option>");
	                	})
                	})
				});
				
				
			}
		})
	});
    
    
   
	
	
	
	//类型筛选
	$('.main').on('click', '.js-classify-file li', function(event) {
		var ele = $(this),type=ele.text();
		$("#classifyType").text(ele.text());
		var folderId = $("#folderList").find("li.selected").attr("data-folder-id");
		$.post("/edu/jsonResource",{folderId:folderId,type:type},function(json){
			if(json.succ == "true"){
				$(".re-list").html("");
				$(".re-list").html(json.html);
			}
		})
	})
	
	
	$(".main").on("click",".shareDownloadUrl",function(){
		var fileId = $(this).attr("data-id");
		$.get("/edu/jsonShareCode",{fileId:fileId},function(json){
			layer.open({
				type: 1,
				title: '分享下载地址',
				area: ['auto', '460px'],
				btn: ['确定', '取消'],
				content: json.html,
				yes: function(index) {
					layer.close(index);
				},
				no: function(index){
					layer.close(index);
				}
			});
			
			
			 $('.switch').on('click', 'li', function() {
				 $(this).addClass("selected").siblings().removeClass("selected");
				 var _index = $('.switch li').index(this);
				 $(".shr-main> div").eq(_index).show().siblings().hide();
				 var isShareCode =$(this).attr("data-status");
				 $.post("/edu/switchCode",{isShareCode:isShareCode,fileId:fileId},function(result){
					 if(result.resultStatus){
						 $("#shareCode").text(result.url);
					 }
				 })
			 });
			 
			 
			//复制邀请码
			var copyToClip = function(eles){
					//定义一个新的复制对象
					var clip = new ZeroClipboard(eles, {
						  moviePath: "/public/js/zeroclipboard-1.2.3/ZeroClipboard.swf"
					} );
				    clip.setHandCursor(true); 
					// 复制内容到剪贴板成功后的操作
					clip.on('complete', function(client, args) {
						layer.confirm('分享链接已经复制到剪贴版中，您可以使用Ctrl-V粘贴出来。',function(){
							layer.closeAll();
						});
					} ); 
			}
			copyToClip($(".copyToClip"));
			
			//重置安全码
			$(".resetShareCode").on("click",function(){
				var fileId = $(this).attr("data-id");
				$.post("/edu/resetShareCode",{fileId:fileId},function(json){
					if(json.resultStatus){
						 $("#shareCode").text(json.url);
					}
				})
			})

		})
		
		
	})
	
	
})