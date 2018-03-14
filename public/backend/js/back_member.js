$(function(){


	var sideBarBox = $("#sideBarBox");//sidevar父容器


	$("#sidebarFoldBtn").on('click', function() {
		if ($(this).attr("title") === "展开") {
			$(this).attr("title","收起").parent().animate({left:0});
			$("#genericGroupManageDiv").animate({marginLeft:'411px'});
		} else {
			$(this).attr("title","展开").parent().animate({left:'-411px'})
			$("#genericGroupManageDiv").animate({marginLeft:'0px'});
		}
	});

	//获取初始根节点
	var getRootNode = function(){
		return $(sideBarBox).find(".genericGroupNode").eq(0);
	}

	//获取当前选中节点
	var getCurrNode = function(){
		return $(sideBarBox).find(".genericGroupNode.nav-on");
	}



	//选中节点
	sideBarBox.on('click',".genericGroupNode",function(){
		var ele = $(this);
		var currNode = getCurrNode();
		$.ajax({
			url:"/edu/back/switchGenericGroup",
			data:{
				groupId: ele.attr("data-id")
			},
			success: function(json){
				if(json.succ){
					currNode.removeClass("nav-on");
					ele.addClass("nav-on");
					$("#genericGroupManageDiv").html(json.html);
				}
			}
		});
	});


	$(".nav-class-box").on('click', '.ui-tree', function(e) {
		e.stopPropagation();
		$(this).parent().next().slideToggle('fast');
	}).on('click', '.nav', function() {
		if($(this).hasClass('nav-on')){
			$(this).find('.node-name').attr('contenteditable', true).focus();
		} else {
			$(this).parents('.bs-sidebar').find('.nav-on').removeClass('nav-on');
			$(this).addClass('nav-on');
		}
	}).on('blur', '.nav', function() {
		var ele=$(this);
		ele.find('.node-name').attr('contenteditable', false);
		var groupName=ele.find(".node-name").text().trim();
		$.ajax({
			url:"/edu/back/updateGroupName",
			data:{
				groupId: ele.attr("data-id"),
				groupName:groupName
			},
			success: function(json){
				if(json.resultStatus){
					layer.msg("修改成功！");
				}
			}
		});

	});;

	$('#createGeneric').on('click', function() {
		var currNode=getCurrNode();
		var data=currNode.attr("data-id");
		var cont =  '<div class="toper-form">'+
					'	<div class="form-group">'+
					'		<label class="lab" style="width:80px;">年级(系)名称</label>'+
					'		<div class="inb">'+
					'			<input class="ipt" type="text" id="groupName">'+
					'		</div>'+
					'	</div>'+
					'</div>';
		layer.open({
			type: 1,
			title: '新建年级',
			area: ['420px', 'auto'],
			btn: ['确定', '取消'],
			content: cont,
			yes: function(index) {
				var groupName = $("#groupName").val();
				$.ajax({
					url:'/edu/back/addGenericGroup',
					contentType: "text/plain; charset=utf-8",
					data:{
						groupName:groupName,
						parentGroupId:data
					},
					success:function(data){
						if(data.resultStatus){
							var genericGroupId= data.data;
							var htmlStr='<div class="ml20"><div id="genericGroupNode_'+genericGroupId+'" class="nav cnode rel genericGroupNode " data-id="'+genericGroupId+'">'+
									   '	<span class="node-name">'+groupName+'</span>'+
									   '	<i class="i i-04 ui-tree"></i>'+
									   '	<i class="i i-08 ui-right ui-arrow"></i>'+
									   '</div></div>';
							currNode.parent().append(htmlStr);
							sideBarBox.find("#genericGroupNode_"+genericGroupId).click();
						}else{
							layer.msg(data.errorMsg);
						}
					}
				})
				layer.close(index);
			},
			no: function(index){
				layer.close(index);
			}
		});
	});


	$('#createClass').on('click', function() {
		var currNode=getCurrNode();
		var data=currNode.attr("data-id");
		var cont =  '<div class="toper-form">'+
					'	<div class="form-group">'+
					'		<label class="lab" style="width:80px;">班级名称</label>'+
					'		<div class="inb">'+
					'			<input class="ipt" type="text" id="clazzName">'+
					'		</div>'+
					'	</div>'+
					'</div>';
		layer.open({
			type: 1,
			title: '新建班级',
			area: ['420px', 'auto'],
			btn: ['确定', '取消'],
			content: cont,
			yes: function(index) {
				var clazzName = $("#clazzName").val();
				$.ajax({
					url:'/edu/back/addClazzGroup',
					data:{
						groupName:clazzName,
						parentGroupId:data
					},
					success:function(data){
						if(data.resultStatus){
							var genericGroupId= data.data;
							var htmlStr='<div class="ml20"><div id="genericGroupNode_'+genericGroupId+'" class="nav cnode rel genericGroupNode " data-id="'+genericGroupId+'">'+
									   '	<span class="node-name">'+clazzName+'</span>'+
									   '	<i class="i i-04 ui-tree"></i>'+
									   '	<i class="i i-08 ui-right ui-arrow"></i>'+
									   '</div></div>';
							currNode.parent().append(htmlStr);
							sideBarBox.find("#genericGroupNode_"+genericGroupId).click();
						}else{
							layer.msg(data.errorMsg);
						}
					}
				})
				layer.close(index);
			},
			no: function(index){
				layer.close(index);
			}
		});
	});



	//上移组织
	sideBarBox.on('click',"#upGenericGroup",function(){
		var currNode = getCurrNode();
		$.ajax({
			url:"/edu/back/upGroup",
			data:{
				groupId: currNode.attr("data-id")
			},
			success: function(json){
				if(json.resultStatus){
					currNode.parent().prev().before(currNode.parent());
				}else{
					layer.msg(json.errorMsg);
				}
			}
		});
	});

	//下移组织
	sideBarBox.on('click',"#downGenericGroup",function(){
		var currNode = getCurrNode();
		$.ajax({
			url:"/edu/back/downGroup",
			data:{
				groupId: currNode.attr("data-id")
			},
			success: function(json){
				if(json.resultStatus){
					currNode.parent().next().after(currNode.parent());
				}else{
					layer.msg(json.errorMsg);
				}
			}
		});
	});

	//删除组织
	sideBarBox.on('click',"#delGenericGroup",function(){
		var currNode = getCurrNode();
		layer.confirm('确定需要删除"'+ currNode.text().trim() +'"', {
		  btn: ['确定','取消'] //按钮
		}, function(index){
			$.ajax({
				url:"/edu/back/delGroup",
				data:{
					groupId: currNode.attr("data-id")
				},
				success: function(json){
					if(json.resultStatus){
						layer.msg("删除成功！")
						currNode.parent().remove();
						getRootNode().click();
					}else{
						layer.msg(json.errorMsg);
					}
				}
			});
		}, function(){

		});
	});




	//教职工学生tab切换
		$("#genericGroupManageDiv").on('click','.memberTabSwitch',function(){
			var ele = $(this);
			var personClass = ele.attr("data-type");
				$.ajax({
					url: "/edu/back/switchGroupMember",
					data:{
						groupId: getCurrNode().attr("data-id"),
						personClass:personClass
					},
					success:function(json){
						if(json.succ){
							if(!ele.hasClass("bs_table_type_on")){
								if("Teacher"==personClass){

								}else if("Student"==personClass){

								}
								$(".memberTabSwitch").removeClass("on");
								ele.addClass("on");
							}
							$("#genericGroupMemberManageDiv").html(json.html);
						}
					}
				});
		});


		/***********人员的增删改操作********************************************************************************************************/

		//全选 反选
		$("#genericGroupManageDiv").on('click','#checkAll',function(){
			var ele = $(this),isChecked = ele.prop("checked");
			$('input[name="id"]').each(function(index,item){
				$(item).prop("checked",isChecked);
			});
		})

		//删除

		$("#genericGroupManageDiv").on('click','#delMember',function(){
			var memberIds =[];
			$('input[name="id"]').each(function(index,item){
				if($(item).prop("checked")){
					memberIds.push($(item).attr("data-id"));
				};
			});
			if(memberIds.length==0){
				layer.msg("请选中用户再进行删除操作！");
			}else{
				layer.confirm('确认删除用户？', {
				  btn: ['确定','取消'] //按钮
				}, function(){
					$.ajax({
						url: "/edu/back/deleteGroupMember",
						data:{
							groupId: getCurrNode().attr("data-id"),
							memberIds:memberIds
						},
						success:function(json){
							if(json.resultStatus){
								layer.msg("删除成功！");
								$(".memberTabSwitch.on").click();
							}else{
								layer.msg(json.errorMsg);
							}
						}
					})
				});
			}
		})
		//导入教师

		$("#genericGroupManageDiv").on('click','#importTeacher',function(){
			var url = '/edu/back/importGroupTeacher',
			downloadUrl = '/Application/downloadTemplate?templateName=teachertemplate.xls';
			var groupId= getCurrNode().attr("data-id");
			var cont =  '<div class="toper-form">'+
						'	<form action="'+ url +'" method="POST" enctype="multipart/form-data" id="fileForm" name="fileForm">'+
						'		<input type="file" name="file">'+
						'		<input type="hidden" name="groupId" value="'+groupId+'">'+
						'		<a href="'+ downloadUrl +'">导入模版下载</a>'+
						'	</form>'+
						'</div>';

			layer.open({
				type: 1,
				title: '导入成员',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var form = $("form[name=fileForm]");
					 var options  = {
			            type:'post',
			            success:function(json){
			            	if(json.status){
			            		layer.close(index);
    							if(json.data.trim().length>0){
    								layer.confirm(json.data, {
									  btn: ['确定','取消'] //按钮
									}, function(){
									}, function(){
									});
    							}
    							$(".memberTabSwitch.on").click();

    						}
			            }
			        };
			        form.ajaxSubmit(options);

				},
				no: function(index){
					layer.close(index);
				}
			});

		})
		//添加教师
		$("#genericGroupManageDiv").on('click','#addTeacher',function(){
			var ele=$(this),groupId= getCurrNode().attr("data-id");
			var cont =  '<div class="toper-form" id="addTeacherBox">'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">用户名</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">手机号</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">工号</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">职位</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">学科</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'</div>';
			layer.open({
				type: 1,
				title: '添加教师',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var inputArray = $("#addTeacherBox input");
					if(isEmpty(inputArray.eq(0).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(1).val().trim(),"不能为空！"))return false;
					if(!isMobil(inputArray.eq(1).val().trim())){layer.msg("手机格式不正确！");return false;}
					if(isEmpty(inputArray.eq(2).val().trim(),"不能为空！"))return false;
					/*if(isEmpty(inputArray.eq(3).val().trim(),"不能为空！"))return false;*/
					/*if(!isEmail(inputArray.eq(3).val().trim())){layer.msg("邮箱格式不正确！");return false;}*/
					/*if(isEmpty(inputArray.eq(4).val().trim(),"不能为空！"))return false;*/
					$.ajax({
						url: "/edu/back/addGroupTeahcer",
						data:{
							groupId: getCurrNode().attr("data-id"),
							"teacher.fullName":inputArray.eq(0).val().trim(),
							"teacher.cellPhone":inputArray.eq(1).val().trim(),
							"teacher.number":inputArray.eq(2).val().trim(),
							"teacher.job":inputArray.eq(3).val().trim(),
							"teacher.subjectArea":inputArray.eq(4).val().trim()
						},
						success:function(json){
							if(json.resultStatus){
								layer.msg("添加成功！");
								$(".memberTabSwitch.on").click();
								layer.close(index);
							}else{
								layer.msg(json.errorMsg);
							}
						}
					})

				},
				no: function(index){
					layer.close(index);
				}
			});

		})
		
		//添加校长
		$("#genericGroupManageDiv").on('click','#addPrincipal',function(){
			var ele=$(this),groupId= getCurrNode().attr("data-id");
			var cont =  '<div class="toper-form" id="addPrincipalBox">'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">用户名</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">手机号</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">工号</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">邮箱</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'</div>';
			layer.open({
				type: 1,
				title: '添加校长',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var inputArray = $("#addPrincipalBox input");
					if(isEmpty(inputArray.eq(0).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(1).val().trim(),"不能为空！"))return false;
					if(!isMobil(inputArray.eq(1).val().trim())){layer.msg("手机格式不正确！");return false;}
					if(isEmpty(inputArray.eq(2).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(3).val().trim(),"不能为空！"))return false;
					if(!isEmail(inputArray.eq(3).val().trim())){layer.msg("邮箱格式不正确！");return false;}
					$.ajax({
						url: "/edu/back/addGroupPrincipal",
						data:{
							groupId: getCurrNode().attr("data-id"),
							"teacher.fullName":inputArray.eq(0).val().trim(),
							"teacher.cellPhone":inputArray.eq(1).val().trim(),
							"teacher.number":inputArray.eq(2).val().trim(),
							"teacher.email":inputArray.eq(3).val().trim()
						},
						success:function(json){
							if(json.resultStatus){
								layer.msg("添加成功！");
								$(".memberTabSwitch.on").click();
								layer.close(index);
							}else{
								layer.msg(json.errorMsg);
							}
						}
					})

				},
				no: function(index){
					layer.close(index);
				}
			});

		})


		//引入教师
		$("#genericGroupManageDiv").on('click','#pullTeacher',function(){
			$.ajax({
				url: "/edu/back/pullGroupTeacher",
				data:{
					groupId: getCurrNode().attr("data-id")
				},
				success:function(json){
					if(json.succ){
						layer.open({
							type: 1,
							title: '引入教师',
							area: ['620px', 'auto'],
							btn: ['确定', '取消'],
							content: json.html,
							yes: function(index) {
								var memberIds=[];
								$("#pullTeacherTable").find("tbody tr").each(function(){
									if($(this).find(":checkbox").prop("checked")){
										memberIds.push($(this).find(":checkbox").attr("data-id"));
									}
								});
								$.ajax({
									url: "/edu/back/addGroupMember",
									type:'POST',
									data:{
										groupId: getCurrNode().attr("data-id"),
										memberIds: memberIds
									},
									success:function(json){
										if(json.resultStatus){
											layer.close(index);
											$(".memberTabSwitch.on").click();
										}
									}
								});
							},
							no: function(index){
								layer.close(index);
							}
						});
					}
				}
			})

		})

		//导入学生
		$("#genericGroupManageDiv").on('click','#importStudent',function(){
			var url = '/edu/back/importGroupStudent',
			downloadUrl = '/Application/downloadTemplate?templateName=studenttemplate.xls';
			var groupId= getCurrNode().attr("data-id");
			var cont =  '<div class="toper-form">'+
						'	<form action="'+ url +'" method="POST" enctype="multipart/form-data" id="fileForm" name="fileForm">'+
						'		<input type="file" name="file">'+
						'		<input type="hidden" name="groupId" value="'+groupId+'">'+
						'		<a href="'+ downloadUrl +'">导入模版下载</a>'+
						'	</form>'+
						'</div>';

			layer.open({
				type: 1,
				title: '导入成员',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var form = $("form[name=fileForm]");
					 var options  = {
			            type:'post',
			            success:function(json){
			            	if(json.status){
			            		layer.close(index);
    							if(json.data.trim().length>0){
    								layer.confirm(json.data, {
									  btn: ['确定','取消'] //按钮
									}, function(){
									}, function(){
									});
    							}
    							$(".memberTabSwitch.on").click();

    						}
			            }
			        };
			        form.ajaxSubmit(options);

				},
				no: function(index){
					layer.close(index);
				}
			});

		})

		//添加学生
		$("#genericGroupManageDiv").on('click','#addStudent',function(){
			var ele=$(this),groupId= getCurrNode().attr("data-id");
			var cont =  '<div class="toper-form" id="addStudentBox">'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">学籍号</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">姓名</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">身份证</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'	<div class="form-group">'+
			'		<label class="lab" style="width:80px;">性别</label>'+
			'		<div class="inb">'+
			'			<input class="ipt" type="text">'+
			'		</div>'+
			'	</div>'+
			'</div>';
			layer.open({
				type: 1,
				title: '添加学生',
				area: ['420px', 'auto'],
				btn: ['确定', '取消'],
				content: cont,
				yes: function(index) {
					var inputArray = $("#addStudentBox input");
					if(isEmpty(inputArray.eq(0).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(1).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(2).val().trim(),"不能为空！"))return false;
					if(isEmpty(inputArray.eq(3).val().trim(),"不能为空！"))return false;

					$.ajax({
						url: "/edu/back/addGroupStudent",
						data:{
							groupId: getCurrNode().attr("data-id"),
							"student.stuNum":inputArray.eq(0).val().trim(),
							"student.fullName":inputArray.eq(1).val().trim(),
							"student.idNum":inputArray.eq(2).val().trim(),
							"student.sex":inputArray.eq(3).val().trim(),
						},
						success:function(json){
							if(json.resultStatus){
								layer.msg("添加成功！");
								$(".memberTabSwitch.on").click();
								layer.close(index);
							}else{
								layer.msg(json.errorMsg);
							}
						}
					})

				},
				no: function(index){
					layer.close(index);
				}
			});

		});
		
		
		
		
		//选择教师
		$("#genericGroupManageDiv").on('click','#chooseTeacher',function(){
			var groupId= getCurrNode().attr("data-id");
			 $.post("/edu/jsonAddTeacher",{groupId:groupId},function(result){
		    	 if(result.succ){
		    		 layer.open({
		    				type: 1,
		    				title: '添加教师',
		    				area: ['460px', 'auto'],
		    				btn: ['确定', '取消'],
		    				content: result.html,
		    				yes: function(index) {
		    					var sel = $(".tea-l :checked"), teacherIds = [];
		    					for(var i=0; i<sel.length; i++) {
		    						teacherIds.push($(sel[i]).attr("id"));
		    					}
		    					if(teacherIds.length < 1){
		    						layer.close(index);
		    					}else{
		    						$.ajax({
										url: "/edu/back/addGroupMember",
										type:'POST',
										data:{
											groupId: getCurrNode().attr("data-id"),
											memberIds: teacherIds
										},
										success:function(json){
											if(json.resultStatus){
												layer.close(index);
												$(".memberTabSwitch.on").click();
											}
										}
									});
		    					}
		    				},
		    				no: function(index){
		    					layer.close(index);
		    				}
		    			});
		    		 
		    		 $("#chooseGroup").on("change",function(){
		    			 var groupId = $("#chooseGroup").val();
		    			 $("#groupTeachers").html("");
		    			 $.post("/edu/getGroupTeacher",{groupId:groupId},function(result){
		    				 if(result.length == 0){
		    					 $("#groupTeachers").append("<li><label>暂无教师</label></li>");
		    				 }else{
		    					 $.each(result,function(n,value){
	    							 $("#groupTeachers").append("<li><label><input id='"+value.personId+"' type='checkbox'>"+value.personName+"</label></li>");
		    					 })
		    				 }
		    			 })
		    		 })
		    	 }
	    	})

		});
		
		
		

		


});

