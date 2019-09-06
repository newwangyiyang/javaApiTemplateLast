package cn.yiyang.system.exception;

/**
 * @Auther: Administrator
 * @Date: 2018/10/13 14:10
 * @Description: 自定义异常
 */
public class WYYException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public WYYException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public WYYException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

