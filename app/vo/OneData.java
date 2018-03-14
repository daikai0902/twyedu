package vo;

import org.apache.commons.lang.BooleanUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneData extends Data {

	public Long id;
	public Long lut;
	public Integer deleted;

	public OneData() {

	}

	public OneData(long id) {
		this.id = id;
	}

	public OneData(long id, long lut, boolean deleted) {
		this.id = id;
		this.lut = lut;
		this.deleted = BooleanUtils.toIntegerObject(deleted);
	}
}
