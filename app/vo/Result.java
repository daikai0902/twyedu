package vo;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import play.Logger;

import java.io.IOException;

/**
 * 网络请求结果
 *
 * @author gcz
 *
 */
public class Result {
	/**
	 * 返回状态：“succ”表示成功；“fail”表示失败
	 */
	public String status;
	/**
	 * 返回状态码
	 */
	public int code;

	public String message;

	public Long systemTime;

	/**
	 * 数据
	 */
	public Data data;

	public static class StatusCode {
		public static final Object[] SUCCESS = { 20000, "请求成功" };
		public static final Object[] STUDENT_NAME_NULL = { 50001, "孩子姓名为空" };
		public static final Object[] SYSTEM_TOKEN_UNVALID = { 50002, "accesstoken失效" };
		public static final Object[] ERROR_PWD = { 50003, "密码错误" };
		public static final Object[] PERSON_NOT_FOUND = { 50004, "用户不存在" };

	}

	public Result() {
		systemTime = System.currentTimeMillis();
	}

	public static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
	}

	public static String failed(Object[] codemessage) {
		Result result = new Result();
		result.status = "fail";
		result.code = (int) codemessage[0];
		result.message = (String) codemessage[1];
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

	public static String failed(Object[] codemessage, Data data) {
		Result result = new Result();
		result.status = "fail";
		result.code = (int) codemessage[0];
		result.message = (String) codemessage[1];
		result.data = data;
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

	public static String succeed() {
		Result result = new Result();
		result.status = "true";
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = (String) StatusCode.SUCCESS[1];
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

	public static String succeed(String message) {
		Result result = new Result();
		result.status = "succ";
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = message;
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

	public static String succeed(Data data) {
		Result result = new Result();
		result.status = "succ";
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = (String) StatusCode.SUCCESS[1];
		result.data = data;
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

	public static String succeed(Data data, String message) {
		Result result = new Result();
		result.status = "succ";
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = message;
		result.data = data;
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("result failed : " + e.getMessage());
			return null;
		}
	}

}
