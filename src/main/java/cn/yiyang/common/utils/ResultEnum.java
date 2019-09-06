package cn.yiyang.common.utils;

/**
 * @Author wangyiyang
 * @Description //TODO 
 * @Date 9:05 2018/9/10
 **/
public enum ResultEnum {
	
	SUCCESS(0,"请求成功"),
	
	ERROR(1,"请求出错"),
	
	EXCEPTION(2,"请求异常"),

	NOTFOUND(3, "请求接口地址错误"),

    ERRORPARAMATERS(4, "请求参数错误"),

    NOTTOKEN(5, "没有权限访问该接口"),

	NOTALLOW(6, "不支持该请求方法");

	private Integer code;
	
	private String msg;

	ResultEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
