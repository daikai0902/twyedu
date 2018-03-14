package vo;

import java.util.List;

public class PageData extends Data {

	public int page;
	public int size;
	public int totalPage;

	public List<? extends OneData> array;

	public PageData() {

	}

	public PageData(List<? extends OneData> array) {
		this.page = 1;
		this.size = array.size();
		this.totalPage = 1;
		this.array = array;
	}

	public PageData(int page, int size, int total, List<? extends OneData> array) {
		this.page = page;
		this.size = size;
		this.totalPage = (total - 1) / size + 1;
		this.array = array;
	}

}
