var initPageDiv = function(totalPage,currentPage){
	totalPage = parseInt(totalPage);
	currentPage = parseInt(currentPage);
	var div = $(".div-page");
	if(div == null || div == undefined)
		return;
	
	div.empty();
	div.append('<ul class="pagination" data-total = "'+totalPage+'" data-current = "'+currentPage+'" ></ul>');
	
	var pagination = $(".pagination");
	
	var tmp1,tmp2;
	if(currentPage == 1)
		tmp1 = "<li class='disabled'><a href='#'>上一页</a></li>";
	else
		tmp1 = "<li><a href='#' data-page="+(currentPage-1)+">上一页</a></li>";
	
	
	if(currentPage == totalPage)
		tmp2 = "<li class='disabled'><a href='#'>下一页</a></li>";
	else
		tmp2 = "<li><a href='#' data-page="+(currentPage+1)+">下一页</a></li>";
	
	
	pagination.append(tmp1);
	pagination.append("<li ><a href='#' data-page='1'>1</a></li>");
	
	var page1 = currentPage-2;
	var page2 = currentPage+2;
	if(page1<2)
		page1=2;
	if(page2<4)
		page2=4;
	if(page2>totalPage)
		page2=totalPage;
	if(page2>totalPage-1)
		page2=totalPage-1;
	
	if(page1 > 2)
		pagination.append("<li class='disabled'><a href='#'>...</a></li>");
	
	for(var i = page1;i <= page2;i++){
		pagination.append("<li ><a href='#' data-page='"+i+"'>"+i+"</a></li>");
	}
	
	if(page2 < totalPage-1)
		pagination.append("<li class='disabled'><a href='#'>...</a></li>");
	
	if(totalPage > page2 && page2 > 0)
		pagination.append("<li ><a href='#' data-page='"+totalPage+"'>"+totalPage+"</a></li>");
		pagination.append(tmp2);
	
	$("[data-page='"+currentPage+"']").parent().addClass("active");
};


var addEvent = function(loadData){
	$(".div-page").on('click', '.pagination li a', function(){
		var pageNum = $(this).attr("data-page");
		loadData(pageNum);
	});
};


