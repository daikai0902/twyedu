package models.json;

public class JsonSucceed extends JsonModel {
	public boolean status = true;

	public String url;

	public JsonSucceed() {
	}

	public JsonSucceed(String url) {
		this.url = url;
	}
}
