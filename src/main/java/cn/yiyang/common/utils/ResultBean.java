package cn.yiyang.common.utils;


/**
 * @param <T>
 * @author Mxy
 * @time 2018年5月28日 上午9:25:08
 * @description: 统一结果处理
 */
public class ResultBean<T> {

    // 状态码
    private Integer code;

    // 提示信息
    private String msg;

    // 具体的内容
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public ResultBean<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultBean<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultBean<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> ResultBean<T> success(T data) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }
    /**
     * 成功
     *
     * @param
     * @return
     */
    public static <T> ResultBean<T> success() {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 失败
     *
     * @param
     * @return
     */
    public static <T> ResultBean<T> error() {
        return new ResultBean<>(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), null);
    }
    /**
     * 失败
     *
     * @param
     * @return
     */
    public static <T> ResultBean<T> error(String message) {
        return new ResultBean<>(ResultEnum.ERROR.getCode(),message, null);
    }

    /**
     * @Author Administrator
     * @Description //TODO
     * @Date 9:16 2018/9/10
     * @Param
     * @return
     **/
    public static <T> ResultBean<T> error(ResultEnum resultEnum) {
        return new ResultBean<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }
    /**
     * @Author Administrator
     * @Description //TODO
     * @Date 9:16 2018/9/10
     * @Param
     * @return
     **/
    public static <T> ResultBean<T> error(ResultEnum resultEnum, T data) {
        return new ResultBean<>(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static <T>ResultBean<T> error(Integer code, String msg, T data) {
        return new ResultBean<>(code, msg, data);
    }
    /**
     * 异常
     *
     * @param
     * @param data
     * @return
     */

    public static <T> ResultBean<T> exception(T data) {
        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), ResultEnum.EXCEPTION.getMsg(), data);
    }

    public static <T> ResultBean<T> exception(Integer code, String msg) {
        return new ResultBean<>(code, msg,null);
    }
}
